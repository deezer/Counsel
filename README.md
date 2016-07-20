# Counsel

## Foreword 

This library contains a collection of advices, applied through AOP, to 
reduce boilerplate code. 

If you have any issue with some aspects of this library (pun intended), 
let us know, or better, send us a pull request. 

## Usage 

To add this library to your build, add the following line to your app's 
`build.gradle` :

        
        repositories {
            maven { url "https://jitpack.io" }
        }
        dependencies {
            debugCompile 'com.deezer.android:counsel-debug:1.0'
            releaseCompile 'com.deezer.android:counsel:1.0'
        }

You'll also need an AspectJ compliant plugin, for instance : 

        buildscript {
            repositories {
                jcenter()
            }
            dependencies {
                classpath 'com.deezer.gradle.plugin:android-aspectj-plugin:1.0-SNAPSHOT'
            }
        }
        
        
        apply plugin: 'android-aspectj'

## Advices 

Here's a list of Advices that are available in the app, and how you can 
use them. To avoid any magic from happening without your consent, 
all our aspects are conditioned by the use of one or more Annotations. 

### Logging 

## Contributions

If you want to give us some advices (pun intended) or fix some issues, 
we're always glad to receive pull requests.  

