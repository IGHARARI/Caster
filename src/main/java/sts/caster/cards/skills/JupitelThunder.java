package sts.caster.cards.skills;

import static sts.caster.CasterMod.makeCardPath;

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

import sts.caster.CasterMod;
import sts.caster.MagicElement;
import sts.caster.actions.ApplyElementalEffectChanceAction;
import sts.caster.actions.LightningDamageAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.characters.TheCaster;

public class JupitelThunder extends CasterCard {

    public static final String ID = CasterMod.makeID("JupitelThunder");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");

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
        baseDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns = DELAY_TURNS;
        magicNumber = baseMagicNumber = HIT_TIMES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
    	for (int i = 0; i < magicNumber; i++) {
    		actions.add(new LightningDamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AttackEffect.NONE, true));
    	}
    	actions.add(new ApplyElementalEffectChanceAction(p, m, MagicElement.THUNDER, magicNumber));
    	AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, actions));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeDamage(UPGRADE_DAMAGE);
        }
    }
}
