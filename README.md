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
        result = Todoai.getInstance().isDone(tag);
        result = Todoai.getInstance().today().isDone(tag);
        result = Todoai.getInstance().newVersion(context).isDone(tag);
        result = Todoai.getInstance().afterInstall(context).isDone(tag);
        result = Todoai.getInstance().timestampBetween(startTime, endTime).isDone(tag);
        // ...
        result = Todoai.getInstance().countExactly(5).isDone(tag);
        result = Todoai.getInstance().countMoreThan(5).isDone(tag);
        result = Todoai.getInstance().countLessThan(5).isDone(tag);
        result = Todoai.getInstance().countBetween(1, 5).isDone(tag);
        result = Todoai.getInstance().today().countMoreThan(5).isDone(tag);
        result = Todoai.getInstance().newVersion(context).countExactly(5).isDone(tag);
        result = Todoai.getInstance().timestampBetween(startTime, endTime).countLessThan(5).isDone(tag);
        // ...

        result = Todoai.getInstance().autoDone().isDone(tag);
        // ...

        Todoai.getInstance().makeDone(tag);
```

## Thanks
https://github.com/jonfinerty/Once
