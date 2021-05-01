package sts.caster.cards.spells;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.FreezeCardAction;
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

public class Cool extends CasterCard {

    public static final String ID = CasterMod.makeID("Cool");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("cool.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int UPG_DAMAGE = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_DAMAGE = 3;
    private static final int BASE_CARDS_FROZEN = 1;


    public Cool() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseM2 = m2 = BASE_DAMAGE;
        baseSpellDamage = spellDamage = 0;
        magicNumber = baseMagicNumber = BASE_CARDS_FROZEN;
        cardsToPreview = new Frostbite();
        cardsToPreview.applyPowers();
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new FreezeCardAction(magicNumber, false, false, false));
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
            CasterCard frostbite = (CasterCard) c.cardsToPreview;
            frostbite.applyPowers();
            actions.add(new DamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage), AttackEffect.BLUNT_HEAVY));
            actions.add(new QueueDelayedCardAction(frostbite, frostbite.delayTurns, t));
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
