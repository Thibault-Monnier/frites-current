<img src="images/frites_text.png" alt="Frites Logo">

## Welcome !

This is the codebase for the FTC team FRITES (20991 from France) during the 2026-27 BioBuzz season.

## Setting up & Compiling

Start by cloning this repository with one of the following commands:

```bash
git clone https://gitlab.com/ftc-civ/frites/2026.git
git clone git@gitlab.com:ftc-civ/frites/2026.git
```

Make sure you have [Android Studio](https://developer.android.com/studio) installed (this is
required to compile the code), and any version of Java.

Open this directory in Android Studio and let it sync (required if you want to be able to compile
the code, even from the terminal).

To compile, use Android Studio or run the following command (on Linux, you may have to run
`chmod +x ./gradlew` first):

```bash
./gradlew build
```

## Coding guidelines

### Formatting

Reformat all code using:

```bash
./gradlew spotlessApply
```

This _must_ be done before committing, otherwise CI will fail. To avoid doing it manually and make formatting quicker, you can also configure your editor to run `clang-format` on file save. To do this, go to `File > Settings > Tools > File Watchers` and click the _import_ button. Select the `watchers.xml` file at the root of this repository and click _OK_.

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

## License

This program is licensed under the GPLV3 license.

See [LICENSE](LICENSE).
