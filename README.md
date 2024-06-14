# XHUB Digital Screen

## Table of Contents
  - [Basic Classes](#Basic-Classes)
  - [Code Conventions](#Code-Conventions)

### Tests
If it says "Command line is too long. Shorten command line for className or also for JUnit defaultconfiguration."

Please add/update a line in idea.properties

```properties
idea.dynamic.classpath=true
```

Reference:
<https://stackoverflow.com/questions/47926382/how-to-configure-shorten-command-line-method-for-whole-project-in-intellij>
<https://developer.android.google.cn/studio/intro/studio-config.html#customize_ide>

If test code coverage has errors, change VM options to ```-noverify```.

### Remove unused resources

The function "Remove unused resources" bases on lint, so, please change lint.xml before running it.

```xml
    <!--  remove severity="ignore" if u want to check UnusedResources  -->
    <issue id="UnusedResources" severity="ignore">
        <ignore regexp="edmodo-.*" />
        <ignore regexp="res/mipmap-.*" />
        <ignore regexp="res/xml/provider_paths.xml" />
    </issue>
```



## Basic Classes

### Application

### Activity

### Fragment
  
### Codes
Store all request codes and result codes in the `Code` class.

### Session


## Code Conventions

### Kotlin general
- English document: <https://kotlinlang.org/docs/reference/coding-conventions.html>
- Chinese document: <http://www.kotlincn.net/docs/reference/coding-conventions.html>
- Google Developer: <https://developer.android.com/kotlin/style-guide>

### XML ids
XML ids should follow the <i>type_name</i> convention:
```
textview_email
imageview_avatar
tv_email
iv_avatar
```

### String resources
There should be no spaces at the beginning or end of the string resource, because it cannot be recognized on One Sky.
If you want to add spaces at the beginning or end of the string, you should escape it.
```xml
<resources>
    <!-- bad -->
    <string name="any_teachers_at">Any teachers at </string>
    <!-- okay -->
    <string name="any_teachers_at">Any teachers at&#160;</string>
</resources>
```

### Class layout
See <https://kotlinlang.org/docs/reference/coding-conventions.html>
Generally, the contents of a class is sorted in the following order:
> Property declarations and initializer blocks
> Secondary constructors
> Method declarations
> Companion object

### Class property name
Property name is used to define getter name and setter name in Kotlin, so, it should **NOT** start with leading ```m``` Character:
```kotlin
val mGroupId: Long = 0L // bad
val groupId: Long = 0L  // okay
```

### Class property default value
Prefer using primitive type initial value for a primitive type property.
```kotlin
// bad
val userId: Long = -1L
// okay
val userId: Long = 0L
```

### Data class property type
Use nullable types for non-java primitive types (String?-String, User?-User).
```kotlin
// bad
data class Message(
    val creator: User,
    val text: String
)
// okay
data class Message(
    val creator: User?,
    val text: String?
)
```
Prefer non-empty types for java primitive types (Int-int, Long-long, Boolean-boolean).
When using the json parser to convert a null value to a java primitive type value, the primitive type initial value will be used, such as 0, false.
```kotlin
// bad
data class User(
    val enterpriseAccountId: Long? = null
)
// okay
data class User(
    val enterpriseAccountId: Long = 0L
)
```

### Network respond type
Use a nullable type as the type of a network respond. It will reduce exceptions in json conversion.
```kotlin
// bad
fun fetchSteps(): List<TourItem>
// okay
fun fetchSteps(): List<TourItem?>?
```

### Collection-like extension method vs. loop
Prefer Collection-like extension methods instead of loops. Usually this makes the code more concise.
```kotlin
// bad: using loop
if (floating_action_menu != null) {
    for (i in 0 until floating_action_menu.childCount) {
        val child: View = floating_action_menu.getChildAt(i)
        if (child is FloatingActionButton && child.id == View.NO_ID) {
            child.setContentDescription(TeacherTourProvider.CLASSROOM_CREATE_FLOAT)
            break
        }
    }
}
// okay: using collection-like extension
floating_action_menu
        ?.children
        ?.firstOrNull { it is FloatingActionButton && it.id == View.NO_ID }
        ?.let { TourTool.applyTagDesc(it, TeacherTourProvider.CLASSROOM_CREATE_FLOAT) }
```

### Log for exceptions
It is recommended to output logs when catching exceptions, especially when catching top-level exceptions. This will help debug.
```kotlin
// bad
try {
    // do something
} catch (e: Exception) {
}
// okay
try {
    // do something
} catch (e: Exception) {
    LogUtil.e(e)
}
```

### Avoid using ```!!```
Avoid using ```!!```, this can help improve code robustness.
```kotlin
var callback: SomeCallback? = null
// bad
if (callback != null) {
    callback!!.onItemClick()
}
// okay
callback?.onItemClick()
```

### Style of when / if else
Using same style in all branches.
```kotlin
// bad
when (key) {
    STATUS_A ->
        doA()
    else -> doDefault()
}
// style of when (one line)
when (key) {
    STATUS_A -> doA()
    else -> doDefault()
}
// style of when (two lines)
when (key) {
    STATUS_A ->
        doA()
    else ->
        doDefault()
}
// style of when (many lines)
when (key) {
    STATUS_A -> {
        doA()
    }
    else -> {
        doDefault()
    }
}
// style of when (many cases in one line)
when (key) {
    STATUS_A, STATUS_B -> doA()
    else -> doDefault()
}
// style of when (one case in one line)
when (key) {
    STATUS_A,
    STATUS_B -> doA()
    else -> doDefault()
}
// style of when (write the arrow in a new line)
when (key) {
    STATUS_A,
    STATUS_B
    -> doA()
    else
    -> doDefault()
}
```
```kotlin
// bad
if (condition) {
    1
} else 0
// style of if else (one line)
if (condition) 1 else 0
// style of if else (many lines)
if (condition) {
    1
} else {
    0
}
```

### Use similar types and parameter order
When you extend a class, if you use a parameter type and order different from the parent class, it may be difficult to read, especially when the parameter types are similar.
```kotlin
// bad
class MyViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(ViewUtil.inflateView(LAYOUT, parent))
// okay
class MyViewHolder private constructor(view: View): RecyclerView.ViewHolder(view) {
    companion object {
        fun create(parent: ViewGroup): MyViewHolder {
            val view = ViewUtil.inflateView(LAYOUT, parent)
            return MyViewHolder(view)
        }
    }
}
```
