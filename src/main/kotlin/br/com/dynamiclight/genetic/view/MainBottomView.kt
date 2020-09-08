package br.com.dynamiclight.genetic.view

import br.com.dynamiclight.genetic.app.Styles
import br.com.dynamiclight.genetic.viewmodel.GaViewModel
import javafx.scene.layout.Priority
import tornadofx.*

class MainBottomView : View("Status") {
    private val viewModel: GaViewModel by inject()

    override val root = vbox {
        vboxConstraints { paddingAll = 5.0 }
        separator()
        label(viewModel.statusProperty) {
            addClass(Styles.status)
        }
    }

}
