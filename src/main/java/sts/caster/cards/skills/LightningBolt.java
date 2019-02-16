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

public class LightningBolt extends CasterCard {

    public static final String ID = CasterMod.makeID("LightningBolt");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int DELAY_TURNS = 1;
    private static final int BASE_DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 3;

    public LightningBolt() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns = DELAY_TURNS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
		actions.add(new LightningDamageAction(m, new DamageInfo(p, damage, DamageType.NORMAL), AttackEffect.SLASH_VERTICAL));
    	actions.add(new ApplyElementalEffectChanceAction(p, m, MagicElement.THUNDER, 1));
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
