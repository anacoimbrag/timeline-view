# Timeline View

![Deploy](https://github.com/anacoimbrag/timeline-view/workflows/Deploy/badge.svg)
[![Min SDK](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![](https://jitpack.io/v/anacoimbrag/timeline-view.svg)](https://jitpack.io/#anacoimbrag/timeline-view)
[![codecov](https://codecov.io/gh/anacoimbrag/timeline-view/branch/master/graph/badge.svg)](https://codecov.io/gh/anacoimbrag/timeline-view)

Android view for timeline purpose. Ideally made for bank statement or sequential data, for example. 

![Timeline Sample](screenshot/timeline.png)

## Usage

For a working implementation, please take a look at the [sample](https://github.com/anacoimbrag/timeline-view/tree/master/sample)

1. Include library
```groovy
// project/build.gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

```groovy
// app/build.gradle
dependencies {
    implementation "com.github.anacoimbrag:timeline-view:$latest_version"
}
```
Latest version is [![](https://jitpack.io/v/anacoimbrag/timeline-view.svg)](https://jitpack.io/#anacoimbrag/timeline-view)

### Add to your layout

It is very easy to use this lib, you just need to add `<Timeline>` tag in your XML layout or instantiate in your code using the default constructor `Timeline(context)`.

```xml
<com.anacoimbra.android.timeline.Timeline
    android:layout_width="wrap_content"
    android:layout_height="100dp"
    app:bulletIcon="@drawable/ic_expense"
    app:bulletType="round"
    app:linePadding="4dp" />
```

#### Timeline Attributes

| Attribute | Type | Default |
| --- |---| :---: |
| `bulletIcon`        | drawable          | -       |
| `bulletIconTint`    | color             | -       |
| `bulletSize`        | dimension         | `30dp`  |
| `bulletIconPadding` | dimension         | `4dp`   |
| `bulletCornerRadius`<sup>1</sup> | dimension         | `6dp`   | 
| `bulletBackground`  | color \| drawable | ![#7E57C2](https://via.placeholder.com/15/7E57C2/000000?text=+) `#7E57C2`  | 
| `bulletType`        | BulletType        | `round` |
| `bulletSize`        | BulletGravity     | `center`|
| `lineWidth`         | dimension         | `2dp`   |
| `lineColor`         | color             | ![#B9B9B9](https://via.placeholder.com/15/B9B9B9/000000?text=+) `#B9B9B9`  | 
| `linePadding`       | dimension         | `0dp`   |
| `lineDashSize`<sup>2</sup>      | dimension         | `4dp`   |
| `lineDashGap`<sup>3</sup>       | dimension         | `4dp`   |
| `lineType`          | LineType          | `solid` |
| `lineVisibility`    | LineVisibility    | `both`  |

**BulletType**

| Code | XML | |
| --- |---| --- |
| `BulletType.ICON`   | `icon`   | ![Bullet Type Icon](screenshot/bullet-type-icon.png)
| `BulletType.SQUARE` | `square` | ![Bullet Type Square](screenshot/bullet-type-square.png)
| `BulletType.CIRCLE` | `circle` | ![Bullet Type Circle](screenshot/bullet-type-circle.png)
| `BulletType.ROUND`  | `round`  | ![Bullet Type Round](screenshot/bullet-type-round.png)

**BulletGravity**

| Code | XML | |
| --- |---| --- |
| `BulletGravity.TOP`   | `top`   | ![Bullet Gravity Top](screenshot/bullet-gravity-top.png)
| `BulletGravity.CENTER` | `center` | ![Bullet Gravity Center](screenshot/bullet-gravity-center.png)
| `BulletGravity.BOTTOM` | `bottom` | ![Bullet Gravity Bottom](screenshot/bullet-gravity-bottom.png)

**LineType**

| Code | XML | |
| --- |---| --- |
| `LineType.SOLID`  | `solid`  | ![Line Type Solid](screenshot/bullet-gravity-center.png)
| `LineType.DASHED` | `dashed` | ![Line Type Dashed](screenshot/line-type-dashed.png)
| `LineType.DOTTED` | `dotted` | ![Line Type Dotted](screenshot/line-type-dotted.png)

**LineVisibility**

| Code | XML | |
| --- |---| --- |
| `LineVisibility.BOTH`   | `both`   | ![Line Visiblity Both](screenshot/bullet-gravity-center.png)
| `LineVisibility.TOP` | `top` | ![Line Visibility Top](screenshot/line-visibility-top.png)
| `LineVisibility.BOTTOM` | `bottom` | ![Line Visibility Bottom](screenshot/line-visibility-bottom.png)
| `LineVisibility.NONE`  | `none`  | ![Line Visibility None](screenshot/line-visibility-none.png)

<sup>1 • Only works with BulletType `round`</sup>
<sup>2 • Only works with LineType `dashed`</sup>
<sup>3 • Only works with LineType `dashed` or `dotted`</sup>