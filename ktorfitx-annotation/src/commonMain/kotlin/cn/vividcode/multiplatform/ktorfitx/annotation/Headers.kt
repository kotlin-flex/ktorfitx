package cn.vividcode.multiplatform.ktorfitx.annotation

/**
 * 项目名称：ktorfitx
 *
 * 作者昵称：li-jia-wei
 *
 * 创建日期：2024/5/12 16:40
 *
 * 文件介绍：Headers
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Headers(
	vararg val headers: String
)