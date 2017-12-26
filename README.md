# Todoai

[![](https://jitpack.io/v/fbsum/Todoai.svg)](https://jitpack.io/#fbsum/Todoai)

A small Android library to manage todo operations.

## Installation
Step 1. Add the JitPack repository to your build file
```groovy
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```groovy
        dependencies {
	      compile 'com.github.fbsum:Todoai:x.y.z'
	}
```

## Usage
```java
        String tag = "SHOW_TAG";
        result = Todoai.persist().isDone(tag);
        result = Todoai.persist().today().isDone(tag);
        result = Todoai.persist().newVersion(context).isDone(tag);
        result = Todoai.persist().afterInstall(context).isDone(tag);
        result = Todoai.persist().between(startTime, endTime).isDone(tag);
        ...

        result = Todoai.persist().moreThan(5).check(tag);
        result = Todoai.persist().exactly(5).check(tag);
        result = Todoai.persist().lessThan(5).check(tag);
        result = Todoai.persist().today().moreThan(5).check(tag);
        result = Todoai.persist().newVersion(context).exactly(5).check(tag);
        result = Todoai.persist().between(startTime, endTime).lessThan(5).check(tag);
        ...

        result = Todoai.session().isDone(tag);
        result = Todoai.session().today().isDone(tag);
        result = Todoai.session().between(startTime, endTime).isDone(tag);
        ...

        result = Todoai.session().moreThan(5).check(tag);
        result = Todoai.session().exactly(5).check(tag);
        result = Todoai.session().lessThan(5).check(tag);
        result = Todoai.session().between(startTime, endTime).lessThan(5).check(tag);
        ...

        Todoai.persist().makeDone(tag);
        Todoai.session().makeDone(tag);
```

## Thanks
https://github.com/jonfinerty/Once
