package com.lxn.util.net

/**
  *  @author 李晓楠
  *  功能描述: 模拟 请求 鸿洋 接口的实体类
  *  时 间： 2022/10/27 14:27
  */
class WxArticleBean {
    /**
     * id : 408
     * name : 鸿洋
     * order : 190000
     * visible : 1
     */
    var id = 0
    var name: String? = null
    var visible = 0

    override fun toString(): String {
        return "TestBean(id=$id, name=$name, visible=$visible)"
    }
}

/**
 *  功能描述: 模拟网络接口实体类
 *  时 间： 2022/10/27 15:20
 */
data class User(
    val admin: Boolean?,
    val chapterTops: List<Any>?,
    val email: String?,
    val icon: String?,
    val id: Int?,
    val nickname: String?,
    val publicName: String?,
    val username: String?
)