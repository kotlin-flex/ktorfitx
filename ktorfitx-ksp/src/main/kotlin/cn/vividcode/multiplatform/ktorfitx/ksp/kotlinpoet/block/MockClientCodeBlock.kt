package cn.vividcode.multiplatform.ktorfitx.ksp.kotlinpoet.block

import cn.vividcode.multiplatform.ktorfitx.ksp.kotlinpoet.UseImports
import cn.vividcode.multiplatform.ktorfitx.ksp.model.model.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock

/**
 * MockClient 代码块
 */
internal class MockClientCodeBlock(
	private val className: ClassName,
	private val mockModel: MockModel,
) : ClientCodeBlock {
	
	override fun CodeBlock.Builder.buildClientCodeBlock(
		funName: String,
		url: String,
		hasBuilder: Boolean,
		builder: CodeBlock.Builder.() -> Unit,
	) {
		UseImports += mockModel.provider
		UseImports.addImports(mockModel.status.packageName, mockModel.status.simpleNames.first())
		val provider = mockModel.provider.simpleName
		val status = "MockStatus.${mockModel.status.simpleName}"
		val leftRound = mockModel.delayRange[0]
		val rightRound = mockModel.delayRange.let { if (it.size == 1) it[0] else it[1] }
		val delayRange = "${leftRound}L..${rightRound}L"
		val mockClientCode = "this.mockClient.$funName(\"$url\", $provider, $status, $delayRange)"
		if (hasBuilder) {
			beginControlFlow(mockClientCode)
			builder()
			endControlFlow()
		} else {
			addStatement(mockClientCode)
		}
	}
	
	override fun CodeBlock.Builder.buildBearerAuthCodeBlock() {
		addStatement("this@${className.simpleName}.ktorfit.token?.invoke()?.let { bearerAuth(it) }")
	}
	
	override fun CodeBlock.Builder.buildHeadersCodeBlock(
		headersModel: HeadersModel?,
		headerModels: List<HeaderModel>,
	) {
		beginControlFlow("headers")
		headersModel?.headerMap?.forEach { (name, value) ->
			addStatement("append(\"$name\", \"$value\"")
		}
		headerModels.forEach {
			addStatement("append(\"${it.name}\", ${it.varName})")
		}
		endControlFlow()
	}
	
	override fun CodeBlock.Builder.buildQueriesCodeBlock(queryModels: List<QueryModel>) {
		beginControlFlow("queries")
		queryModels.forEach {
			addStatement("append(\"${it.name}\", ${it.varName})")
		}
		endControlFlow()
	}
	
	override fun CodeBlock.Builder.buildPartsCodeBlock(partModels: List<PartModel>) {
		beginControlFlow("parts")
		partModels.forEach {
			addStatement("append(\"${it.name}\", ${it.varName})")
		}
		endControlFlow()
	}
	
	override fun CodeBlock.Builder.buildFieldsCodeBlock(fieldModels: List<FieldModel>) {
		beginControlFlow("fields")
		fieldModels.forEach {
			addStatement("append(\"${it.name}\", ${it.varName})")
		}
		endControlFlow()
	}
	
	fun CodeBlock.Builder.buildPathsCodeBlock(pathModels: List<PathModel>) {
		beginControlFlow("paths")
		pathModels.forEach {
			addStatement("append(\"${it.name}\", ${it.varName})")
		}
		endControlFlow()
	}
	
	override fun CodeBlock.Builder.buildBodyCodeBlock(bodyModel: BodyModel) {
		addStatement("body(${bodyModel.varName})")
	}
}