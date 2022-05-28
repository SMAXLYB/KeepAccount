package life.chenshi.keepaccounts.library.view.guide


sealed class Constraint private constructor(val offset: Float) {
    class TopToTopOfHighlight(offset: Float = 0f) : Constraint(offset)
    class BottomToTopOfHighlight(offset: Float = 0f) : Constraint(offset)
    class BottomToBottomOfHighlight(offset: Float = 0f) : Constraint(offset)
    class TopToBottomOfHighlight(offset: Float = 0f) : Constraint(offset)

    class StartToStartOfHighlight(offset: Float = 0f) : Constraint(offset)
    class StartToEndOfHighlight(offset: Float = 0f) : Constraint(offset)
    class EndToEndOfHighlight(offset: Float = 0f) : Constraint(offset)
    class EndToStartOfHighlight(offset: Float = 0f) : Constraint(offset)

    operator fun plus(constraint: Constraint): List<Constraint> {
        return listOf(this, constraint)
    }
}