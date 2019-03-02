package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.actions.RandomTargetLightningDamageAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;

public class LordOfVermillion extends CasterCard {

    public static final String ID = CasterMod.makeID("LordOfVermillion");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("lordofvermillion.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = -1;
    private static final int DELAY_TURNS = 4;
    private static final int BASE_DAMAGE = 5;
    private static final int HIT_TIMES = 5;
    private static final int UPGR_HIT_TIMES = 2;

    public LordOfVermillion() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns = DELAY_TURNS;
        magicNumber = baseMagicNumber = HIT_TIMES;
        cardElement = MagicElement.THUNDER;
        this.tags.add(TheCaster.Enums.DELAYED_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (energyOnUse < EnergyPanel.totalCount) {
            energyOnUse = EnergyPanel.totalCount;
        }
    	AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, energyOnUse, null));
    	AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
    }
    
    @Override
    public ActionListMaker getActionsMaker(int spentEnergy) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
        	for (int i = 0; i < spentEnergy*c.magicNumber; i++) {
        		actions.add(new RandomTargetLightningDamageAction(new DamageInfo(AbstractDungeon.player, c.spellDamage, DamageType.NORMAL), AttackEffect.NONE));
        	}
    		return actions;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeMagicNumber(UPGR_HIT_TIMES);
        }
    }
}
