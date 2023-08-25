package org.tenggara;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import org.tenggara.annotations.Getter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

import static com.sun.tools.javac.code.Flags.PUBLIC;

@SupportedAnnotationTypes("org.tenggara.annotations.Getter")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class GetterProcessor extends BaseProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager.printMessage(Diagnostic.Kind.NOTE, "========= GetterProcessor init =========");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        final var getterElements = roundEnv.getElementsAnnotatedWith(Getter.class);
        getterElements.forEach(element -> {
            trees.getTree(element).accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    final var classVariableDeclarations = jcClassDecl.defs.stream()
                            .filter(it -> it.getKind() == Tree.Kind.VARIABLE)
                            .map(JCTree.JCVariableDecl.class::cast)
                            .map(it -> makeGetterMethodDecl(it))
                            .toList();

                    classVariableDeclarations.forEach(it -> {
                        messager.printMessage(Diagnostic.Kind.NOTE, it.getName() + " has been processed");
                        jcClassDecl.defs = jcClassDecl.defs.prepend(it);
                    });

                    super.visitClassDef(jcClassDecl);
                }
            });
        });
        return true;
    }

    private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl classVariableDeclaration) {
        final var name = classVariableDeclaration.getName();
        final var field = treeMaker.Select(treeMaker.Ident(names.fromString("this")), name);
        final var returnStatement = treeMaker.Return(field);

        final var modifiers = treeMaker.Modifiers(PUBLIC);
        final var returnType = classVariableDeclaration.vartype;
        final var methodName = names.fromString("get" + name.toString().substring(0, 1).toUpperCase() + name.toString().substring(1, name.length()));
        final var parameterTypes = List.<JCTree.JCTypeParameter>nil();
        final var parameterObjects = List.<JCTree.JCVariableDecl>nil();
        final var exceptions = List.<JCTree.JCExpression>nil();
        final var codeBlock = treeMaker.Block(0, List.of(returnStatement));

        return treeMaker.MethodDef(modifiers, methodName, returnType, parameterTypes, parameterObjects, exceptions, codeBlock, null);
    }
}
