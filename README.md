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

#### Retry

This advice enables you to automatically retry calls to any method up to... 
As many times you want. Here's how you can use it : 

    @RetryOnFailure(retryCount = 3, retryDelayMs = 500)
    private Foo doSomething() {
        Foo result;
        
        // Do something that might throw an exception
        
        return result;
    }

In the annotation, you can configure how many times a method call should 
be retried (by default 3), and how long to wait between each retry 
(by default 500 ms).

#### Poolable

Object Pools are great tools to avoid unnecessary instanciation and garbage 
collection. This advice will create an ObjectPool really easily for you. 

    public class Foo implements Poolable {

        // ... 
        
        @Override
        public void releaseInstance() {
            // This is a good place to cleanup any state
        }
    }
    
So then, how do you use this as an object pool ? Just keep calling `new` 
to get new or recycled instances, and call `releaseInstance()` when you're 
done with your instance to recycle it.
 
    Foo foo = new Foo();
    
    // ... do something with it 
    
    foo.releaseInstance();

As a best practice, it's better to avoid Constructors with parameters, as 
those will be ignored if a recycled instance is available. Prefer the 
following pattern. 

#### Multithreading

We all know the `@UiThread` / `@WorkerThread` annotations that are 
available in Android. But they're just hints to the compiler (developpers) 
on which thread the method is expected to be ran on. 

Problem is, if the method is called by an un-annotated method, it has no 
way to trigger a warning. 
 
With the `@RunOnMainThread` / `@RunOnWorkerThread`, you can actually enforce 
the threads your method should run in, without adding any hassle to the 
callers. If the method is called on the wrong thread, it will automatically 
be wrapped in a Runnable and posted on a handler on the main Looper. 

    @RunOnMainThread
    private void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
 
    @RunOnThread
    private void someHeavyComputing() {
        // ... 
    }
    
## Contributions

If you want to give us some advices (pun intended) or fix some issues, 
we're always glad to receive pull requests. 
 
Make sure that your code is unit tested, and that a sample illustrates 
the modified / new bahvior. 

## License

This library is distributed under the [MIT License](https://opensource.org/licenses/MIT)