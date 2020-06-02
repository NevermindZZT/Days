package android.animation

/**
 * ObjectAnimator 扩展
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

/**
 * 启动属性动画
 * @receiver ObjectAnimator 属性动画
 * @param act [@kotlin.ExtensionFunctionType] Function1<ObjectAnimator, Unit?>? 执行动作
 */
fun ObjectAnimator.start(act: (ObjectAnimator.()->Unit)? = null) {
    act?.invoke(this)
    start()
}
