package sts.caster.core.freeze;

@Deprecated
public class DeprecatedFrozenPileManager {
//    public static CardGroup frozenPile = new CardGroup(CardGroupType.UNSPECIFIED);
//    public static DeprecatedFrozenPileViewScreen frozenPileViewScreen = new DeprecatedFrozenPileViewScreen();
//    public static DeprecatedFrozenCardsPanel frozenPanel = new DeprecatedFrozenCardsPanel();
//    public static Integer frozenCardCountForCombat = 0;
//
//    public static void moveToFrozenPile(CardGroup originalGroup, AbstractCard card) {
//        resetCardBeforeMoving(originalGroup, card);
//        AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
//        frozenPile.addToTop(card);
//        applyOnFrozenTriggers(card);
//        frozenCardCountForCombat++;
//    }
//
//    public static void resetFrozenCount() {
//        frozenCardCountForCombat = 0;
//    }
//
//    public static Integer getFrozenCount() {
//        return frozenCardCountForCombat;
//    }
//
//    private static void applyOnFrozenTriggers(AbstractCard card) {
//        if (AbstractDungeon.player.hasPower(ShivaPower.POWER_ID)) {
//            AbstractDungeon.player.getPower(ShivaPower.POWER_ID).onSpecificTrigger();
//        }
//        if (AbstractDungeon.player.hasPower(ThermodynamicsPower.POWER_ID)) {
//            ((ThermodynamicsPower) AbstractDungeon.player.getPower(ThermodynamicsPower.POWER_ID)).onFreezeCard();
//        }
//        if (card instanceof CasterCard)
//            AbstractDungeon.actionManager.addToBottom(new CardOnFrozenTriggerAction((CasterCard) card));
//    }
//
//    private static void resetCardBeforeMoving(CardGroup originalGroup, AbstractCard c) {
//        if (AbstractDungeon.player.hoveredCard == c) {
//            AbstractDungeon.player.releaseCard();
//        }
//        AbstractDungeon.actionManager.removeFromQueue(c);
//        c.unhover();
//        c.untip();
//        c.stopGlowing();
//        originalGroup.group.remove(c);
//    }
}
