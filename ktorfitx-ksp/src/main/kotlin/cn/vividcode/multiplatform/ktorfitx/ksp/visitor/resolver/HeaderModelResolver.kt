package cn.vividcode.multiplatform.ktorfitx.ksp.visitor.resolver

import cn.vividcode.multiplatform.ktorfitx.annotation.Header
import cn.vividcode.multiplatform.ktorfitx.ksp.expends.getKSAnnotationByType
import cn.vividcode.multiplatform.ktorfitx.ksp.expends.getValue
import cn.vividcode.multiplatform.ktorfitx.ksp.model.model.HeaderModel
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

/**
 * 项目名称：ktorfitx
 *
 * 作者昵称：li-jia-wei
 *
 * 创建日期：2024/7/9 22:12
 *
 * 文件介绍：HeaderModelResolver
 */
internal object HeaderModelResolver {
	
	private val regex = "([a-z])([A-Z])".toRegex()
	
	fun KSFunctionDeclaration.resolves(): List<HeaderModel> {
		return this.parameters.mapNotNull { valueParameter ->
			val annotation = valueParameter.getKSAnnotationByType(Header::class) ?: return@mapNotNull null
			var name = annotation.getValue(Header::name)
			val varName = valueParameter.name!!.asString()
			if (name.isNullOrBlank()) {
				name = varName.replace(regex) {
					"${it.groupValues[1]}-${it.groupValues[2]}"
				}.replaceFirstChar { it.uppercase() }
			}
			HeaderModel(name, varName)
		}
	}
}