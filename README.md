# Mamba


## Mamba
mamba-plugin: [ ![Download](https://api.bintray.com/packages/codelang-organization/maven/mamba-plugin/images/download.svg) ](https://bintray.com/codelang-organization/maven/mamba-plugin/_latestVersion)
mamba-api: [ ![Download](https://api.bintray.com/packages/codelang-organization/maven/mamba-api/images/download.svg) ](https://bintray.com/codelang-organization/maven/mamba-api/_latestVersion)
监听方法的开始和结束<br />

<a name="6860b943"></a>
## 依赖


> 项目目录下的build.gradle

```java
dependencies {
     classpath 'com.codelang:mamba-plugin:1.0.2'
}
```


> app module 下的 build.gradle

```java
apply plugin: 'com.android.application'
apply plugin: 'com.codelang.mamba'

dependencies {
    implementation "com.codelang:mamba-api:1.0.2"
}
mamba {
    include = ["com.kiwi.mamba", "com.codelang.library"]
    exclude = [
            "com.kiwi.mamba.base.MyApplication",
            "com.kiwi.mamba.loader"
    ]
    methodEnable = true
    trackEnable = true
}
```

<br />

## 使用

### 初始化：
```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Mamba.init(new CostTimeLoader());
    }
}

```

<br />Demo 提供了两个 Loader：

- CostTimeLoader : 统计方法耗时
- TrackLoader : 捕捉方法信息



<a name="EBlEW"></a>
### CostTimeLoader
用于统计方法耗时，需要在 app build.gradle  中配置：
```java
mamba {
    include = ["com.kiwi.mamba", "com.codelang.library"]
    exclude = [
            "com.kiwi.mamba.base.MyApplication",
            "com.kiwi.mamba.loader"
    ]
    methodEnable = true
}
```

- include  : 配置需要进行插桩的包路径、类
- exclude : 对包路径、类不进行插桩
- methodEnable :  方法插桩开启，默认为 true


打印信息:
```
com.kiwi.mamba E/time: class=com.kiwi.mamba.MainActivity methodName=<init> costTime=0ms
com.kiwi.mamba E/time: class=com.kiwi.mamba.MainActivity methodName=onCreate costTime=208ms
com.kiwi.mamba E/time: class=com.kiwi.mamba.MainActivity methodName=goPage costTime=14ms
com.kiwi.mamba E/time: class=com.codelang.library.TrackActivity methodName=<init> costTime=0ms
com.kiwi.mamba E/time: class=com.codelang.library.TrackActivity methodName=onCreate costTime=36ms
```




<br />注：Mamba 的初始化，或是实现的 Loader 类，都必须配置 exclude，不然会发生插桩导致循环调用的情况

<a name="g1uZv"></a>
### TrackLoader
用于捕捉方法信息，需要在 app build.gradle 中配置
```java
mamba {
    include = ["com.kiwi.mamba", "com.codelang.library"]
    exclude = [
            "com.kiwi.mamba.base.MyApplication",
            "com.kiwi.mamba.loader"
    ]
    methodEnbale = false
    trackEnable = true
}
```
一般来说，如果想捕捉方法信息的话，可以暂时性关闭 methodEnbale，仅对 @Track 注解的方法进行采集。<br />
<br />如果想捕捉 open 函数的话，可以采用如下方式：
```java
    @Track
    private void open(String t, float a, double b, long c) {
        Toast.makeText(this, "What can I say？Mamba out", Toast.LENGTH_SHORT).show();
    }
```
对应插桩的结果为：
```java
 private void open(String str, float f, double d, long j) {
        Class<TrackActivity> cls = TrackActivity.class;
        Mamba.i(cls, "open", str, Float.valueOf(f), Double.valueOf(d), Long.valueOf(j));
        Toast.makeText(this, "What can I say？Mamba out", 0).show();
 }
```
我们可以通过 Mamba 的 loader 拿到信息，具体可以查看 TrackLoader
```kotlin
  override fun methodEnter(clazz: Class<*>?, methodName: String?, args: Array<out Any>?) {
        when (clazz) {
            TrackActivity::class.java -> {
                trackPage(methodName, args)
            }
        }
   }

    private fun trackPage(methodName: String?, args: Array<out Any>?) {
        when (methodName) {
            "open" -> {
                "触发 $methodName 方法".loge()
                args?.forEach {
                    "捕捉到参数值:$it ".loge()
                }
            }
        }
    }
```
我们可以通过对应的类名和方法名，取出方法的参数值，然后实现无痕业务的埋点方案，我们只需要在业务代码中实现需要捕捉的方法，和需要记录的参数值即可，当然，具体的比较，会出一篇文章来进行详细的讲解
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       [ ![Mamba-Api Download](https://api.bintray.com/packages/codelang-organization/maven/mamba-api/images/download.svg) ](https://bintray.com/codelang-organization/maven/mamba-api/_latestVersion)[ ![Mamba-plugin Download](https://api.bintray.com/packages/codelang-organization/maven/mamba-plugin/images/download.svg) ](https://bintray.com/codelang-organization/maven/mamba-plugin/_latestVersion)