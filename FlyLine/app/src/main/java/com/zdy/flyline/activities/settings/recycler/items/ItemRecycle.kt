package com.zdy.flyline.activities.settings.recycler.items

import com.zdy.flyline.protocol.parameters.MenuParameters
import com.zdy.flyline.protocol.parameters.ParameterInt

sealed class ItemRecycle{

    open class ParameterIntItem(
        val parameterInt: ParameterInt
    ) : ItemRecycle()

    class  ParameterMenuItem(
        val menuParameters: MenuParameters
    ) : ItemRecycle()

    class ParameterTimeItem(
        parameterInt: ParameterInt
    ) : ParameterIntItem(parameterInt)

    class ButtonItem() : ItemRecycle()

}
