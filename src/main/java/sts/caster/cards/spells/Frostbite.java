package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.DelayedDamageAllEnemiesAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.core.frozenpile.FrozenPileManager;
import sts.caster.interfaces.ActionListSupplier;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.makeCardPath;

public class Frostbite extends CasterCard {

    public static final String ID = CasterMod.makeID("Frostbite");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("frostbite.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int UPG_DAMAGE = 1;
    private static final int BASE_DELAY = 2;
    private static final int BASE_DAMAGE = 3;


    public Frostbite() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseM2 = m2 = BASE_DAMAGE;
        baseSpellDamage = spellDamage = 0;
        cardsToPreview = new Fimbulvetr();
        cardsToPreview.applyPowers();
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
    }

    @Override
    public void applyPowers() {
        baseSpellDamage = FrozenPileManager.getFrozenCount() * m2;
        super.applyPowers();
    }

    @Override
    public ActionListSupplier actionListSupplier(Integer energySpent) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
            CasterCard fimbulvetr = (CasterCard) c.cardsToPreview;
            fimbulvetr.applyPowers();
            actions.add(new DelayedDamageAllEnemiesAction(AbstractDungeon.player, c.spellDamage, c.cardElement, AttackEffect.SMASH));
            actions.add(new QueueDelayedCardAction(fimbulvetr, fimbulvetr.delayTurns, t));
            return actions;
        };
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            cardsToPreview.upgrade();
            upgradeM2(UPG_DAMAGE);
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
