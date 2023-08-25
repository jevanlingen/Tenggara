Simple annotation processor, to learn how Lombok works...

## Usage

- Run `mvn install`
- Use in another project:
```xml
<dependency>
    <groupId>org.tenggara</groupId>
    <artifactId>tenggara</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
- Hit "Build Project" in IntelliJ

> :warning: **Works only in IntelliJ yet**: If you run this with maven, you will get a `Compilation failure Bad service configuration file, or exception thrown while constructing Processor object: javax.annotation.processing.Processor: org.tenggara.GetterProcessor Unable to get public no-arg constructor` error. As this repo is just to discover how Lombok does work, this error will probably not be fixed!
