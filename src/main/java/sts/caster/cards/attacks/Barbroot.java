package sts.caster.cards.attacks;

import static sts.caster.core.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.actions.ArbitraryCardAction;
import sts.caster.actions.BarbrootAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.patches.spellCardType.CasterCardType;

import java.util.ArrayList;

public class Barbroot extends CasterCard {

    public static final String ID = CasterMod.makeID("Barbroot");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Attack.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int CAST_TIME = 1;

    public Barbroot() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        baseDelayTurns = delayTurns = CAST_TIME;
        setCardElement(MagicElement.EARTH);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo damageInfo = new DamageInfo(p, damage);
		addToBot(new BarbrootAction(damageInfo, m));
        int damageDealt = Math.min(damageInfo.output - m.currentBlock, m.currentHealth);
        baseMagicNumber = magicNumber =  Math.max(damageDealt, 0) / 4;
        rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
        addToBot(new QueueDelayedCardAction(this, delayTurns, m));
        addToBot(new ArbitraryCardAction(this, (c) -> {
            c.rawDescription = cardStrings.DESCRIPTION;
            c.initializeDescription();
        }));
    }

    @Override
    public ActionListMaker buildActionsSupplier(Integer energySpent) {
        return (c, t) -> {
            ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
            if (magicNumber > 0) {
                addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber));
            }
            return actionsList;
        };
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
