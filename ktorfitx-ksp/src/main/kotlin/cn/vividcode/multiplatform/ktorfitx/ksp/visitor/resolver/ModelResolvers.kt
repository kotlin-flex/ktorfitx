package cn.vividcode.multiplatform.ktorfitx.ksp.visitor.resolver

import cn.vividcode.multiplatform.ktorfitx.ksp.check.compileCheck
import cn.vividcode.multiplatform.ktorfitx.ksp.model.model.*
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

/**
 * 项目名称：ktorfitx
 *
 * 作者昵称：li-jia-wei
 *
 * 创建日期：2024/7/6 9:42
 *
 * 文件介绍：ModelResolvers
 */
internal object ModelResolvers {
	
	/**
	 * 获取所有 FunctionModel
	 */
	fun KSFunctionDeclaration.getAllFunctionModels(resolver: Resolver): List<FunctionModel> {
		val models = mutableListOf<FunctionModel?>()
		models += with(WebSocketResolver) { resolve() }
		models += with(ApiModelResolver) { resolve(models.any { it is WebSocketModel }) }
		models += with(HeadersModelResolver) { resolve() }
		models += with(MockModelResolver) { resolve() }
		models += with(BearerAuthModelResolver) { resolve() }
		models += with(ExceptionListenerResolver) { resolves(resolver) }
		return models.filterNotNull()
	}
	
	/**
	 * 获取所有 ParameterModel
	 */
	fun KSFunctionDeclaration.getAllParameterModel(isWebSocket: Boolean): List<ParameterModel> {
		return with(ParameterModelResolver) { resolves(isWebSocket) }
	}
	
	/**
	 * 获取所有 ValueParameterModel
	 */
	fun KSFunctionDeclaration.getAllValueParameterModels(): List<ValueParameterModel> {
		val models = mutableListOf<ValueParameterModel?>()
		models += with(BodyModelResolver) { resolve() }
		models += with(QueryModelResolver) { resolves() }
		models += with(PartModelResolver) { resolves() }
		models += with(FieldModelResolver) { resolves() }
		models += with(PathModelResolver) { resolves() }
		models += with(HeaderModelResolver) { resolves() }
		val filterModels = models.filterNotNull()
		val incompatibleTypeCount = filterModels.mapNotNull {
			when (it) {
				is BodyModel -> BodyModel::class
				is PartModel -> PartModel::class
				is FieldModel -> FieldModel::class
				else -> null
			}
		}.toSet().size
		this.compileCheck(incompatibleTypeCount <= 1) {
			val funName = this.simpleName.asString()
			"$funName 方法不能同时使用 @Body, @Part 和 @Field 注解"
		}
		return models.filterNotNull()
	}
}