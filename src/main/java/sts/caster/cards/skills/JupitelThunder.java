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

import sts.caster.actions.ApplyElementalEffectChanceAction;
import sts.caster.actions.LightningDamageAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;

public class JupitelThunder extends CasterCard {

    public static final String ID = CasterMod.makeID("JupitelThunder");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("jupitelthunder.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int DELAY_TURNS = 2;
    private static final int BASE_DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int HIT_TIMES = 4;

    public JupitelThunder() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns = DELAY_TURNS;
        magicNumber = baseMagicNumber = HIT_TIMES;
        cardElement = MagicElement.THUNDER;
        this.tags.add(TheCaster.Enums.DELAYED_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, m));
    }
    
    @Override
    public ActionListMaker getActionsMaker() {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
        	for (int i = 0; i < c.magicNumber; i++) {
        		actionsList.add(new LightningDamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage, DamageType.NORMAL), AttackEffect.NONE, true));
        	}
        	actionsList.add(new ApplyElementalEffectChanceAction(AbstractDungeon.player, t, MagicElement.THUNDER, c.magicNumber));
    		return actionsList;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeSpellDamage(UPGRADE_DAMAGE);
        }
    }
}
