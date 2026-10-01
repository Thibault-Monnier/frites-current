## Coding guidelines

### Formatting

Reformat all code using:

```bash
./gradlew spotlessApply
```

This _must_ be done before committing, otherwise CI will fail. To avoid doing it manually and make
formatting quicker, you can also configure your editor to run `clang-format` on file save. To do
this, go to `File > Settings > Tools > File Watchers` and click the _import_ button. Select the
`watchers.xml` file at the root of this repository and click _OK_. Then, go to
`File > Settings > Tools > Actions on Save` and make sure `File Watcher` action is enabled.

### Casing

- __Classes and enums__ use _PascalCase_
- __Objects, variables and functions__ use _camelCase_
- __Constants and enum members__ use _CONSTANT_CASE_

```java
class Class { /*...*/
}

enum Enum {
    FIRST_MEMBER,
    SECOND_MEMBER
}

int exampleVariable;

public void exampleFunction() { /*...*/ }
```
