# ACheckableWordList
This is a simple Android library implementing a (comma) separated word list where each word can be clicked and cheched/unchecked.

[ ![Methods count](https://img.shields.io/badge/Methods count-42-e91e63.svg)](http://www.methodscount.com/?lib=it.xabaras.android%3Aacheckablewordlist%3A1.0.1)
[ ![Size](https://img.shields.io/badge/Size-3KB-e91e63.svg)](http://www.methodscount.com/?lib=it.xabaras.android%3Aacheckablewordlist%3A1.0.1)
[ ![Download](https://api.bintray.com/packages/xabaras/maven/acheckablewordlist/images/download.svg) ](https://bintray.com/xabaras/maven/acheckablewordlist/_latestVersion)

## How do I get set up? ##

Get it via Gradle
```groovy
compile 'it.xabaras.android:acheckablewordlist:1.0.1'
```
or Maven
```xml
<dependency>
  <groupId>it.xabaras.android</groupId>
  <artifactId>acheckablewordlist</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

Or download the [latest AAR](https://bintray.com/xabaras/maven/acheckablewordlist/_latestVersion) and add it to your project's libraries.

## Usage ##

Here is a non-comprehensive guide to ACheckableWordList for any further iformation you can reference the library [javadoc](https://xabaras.github.io/ACheckableWordList/javadoc/), the sources and/or the sample app sources.

You just have to add ACheckableWordList to your xml layout the usual way:

```xml
<it.xabaras.android.acheckablewordlist.widget.ACheckableWordList
        android:id="@+id/wordList"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```


#### Adding words to the list ####

You can basically add words to ACheckableWordList one by one

```java
wordList.addWord("xabaras");
```

or as a list

```java
wordList.setWords(Arrays.asList(new String[]{"Hello", "ACheckableWordList", "This", "is", "a", "sample"}));
```

#### Handling item click and selection ####

ACheckableWordList provides an OnItemClickListener to catch click and selection events.
You just have to register your listener as usual:
```java
wordList.setOnItemClickListener(new ACheckableWordList.OnItemClickListener() {
    @Override
    public void onItemClick(View v, int position, boolean isSelected) {
        Snackbar.make(v, String.format("Word %d clicked, %s", position, isSelected ? "selected" : "unselected") , Snackbar.LENGTH_SHORT)
            .show();
    }
});
```

#### Styling ####
You can customize the look and feel of ACheckableWordList by adding the following section to your styles.xml file

```xml
<style name="ACheckableWordList">
    <item name="acwl_text_color">@color/acwl_text_color</item>
    <item name="acwl_horizontal_spacing">5dp</item>
    <item name="acwl_vertical_spacing">8dp</item>
    <item name="acwl_selection_enabled">true</item>
    <item name="acwl_text_size">18sp</item>
    <item name="acwl_separator">,</item>
</style>
```

Here follows a list of supported attributes:
##### acwl_horizontal_spacing #####
Defines the distance between two consecutive words on the same line, **4dp by default**, e.g
```xml
<item name="acwl_horizontal_spacing">5dp</item>
```

##### acwl_vertical_spacing #####
Defines the distance between two consecutive lines of the word list, **8dp by default**, e.g
```xml
<item name="acwl_vertical_spacing">8dp</item>
```

##### cwl_selection_enabled #####
Tells if the words in the list can be selected, **false by default**, e.g
```xml
<item name="acwl_selection_enabled">true</item>
```

##### acwl_text_color #####
Is a selector resource, it defines text colors for the normal, pressed, selected and disabled states.
You can customize it by defining a selector in your **color** resource folder and adding this tag to the ACheckableWordList style
```xml
<item name="acwl_text_color">@color/acwl_text_color</item>
```
Here is an example of how the acwl_text_color resource should look like:
```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_enabled="false"
        android:color="@color/disabled_text"/> <!-- disabled -->
    <item android:state_pressed="true"
        android:color="@color/colorAccent" /> <!-- pressed -->
    <item android:state_selected="true"
        android:color="@color/colorPrimary" /> <!-- selected -->
    <item android:color="@color/default_text" /> <!-- default -->
</selector>
```

#####a cwl_text_size ####
Define the textSize attribute of the words added to te word list, **14sp by default**, e.g
```xml
<item name="acwl_text_size">18sp</item>
```

##### acwl_separator #####
By default ACheckableWordList comes with no separator character between words, but you can add one in your style by defining the following tag
```xml
<item name="acwl_separator">,</item>
```
