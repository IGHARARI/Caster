package sts.caster.cards.spells;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.DelayedDamageAllEnemiesAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.RecurringSpellCardMod;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.interfaces.ICardWasIgnitedSubscriber;
import sts.caster.patches.delayedCards.CastingQueueGroupEnum;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class FireWall extends CasterCard implements ICardWasIgnitedSubscriber {

    public static final String ID = CasterMod.makeID("FireWall");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("ashes.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int CAST_TIME = 2;
    private static final int BASE_DAMAGE = 10;
    private static final int UPG_DAMAGE = 4;
    private static final int ECHO_FF_GAIN = 1;

    public FireWall() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = CAST_TIME;
        spellDamage = baseSpellDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = ECHO_FF_GAIN;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new QueueDelayedCardAction(this, delayTurns, null));
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            actionsList.add(new DelayedDamageAllEnemiesAction(AbstractDungeon.player, c.spellDamage, c.cardElement, AbstractGameAction.AttackEffect.FIRE));
            return actionsList;
        };
    }

    @Override
    public void cardWasIgnited(CardGroup.CardGroupType gType) {
        if (gType == CastingQueueGroupEnum.CASTER_CASTING_QUEUE) {
            this.flash(Color.FIREBRICK.cpy());
            RecurringSpellCardMod.addRecurrence(this, magicNumber);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSpellDamage(UPG_DAMAGE);
        }
    }
}
