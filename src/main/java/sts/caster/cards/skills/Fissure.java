package sts.caster.cards.skills;

import static sts.caster.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.IronWaveEffect;

import sts.caster.CasterMod;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.characters.TheCaster;

public class Fissure extends CasterCard {

    public static final String ID = CasterMod.makeID("Fissure");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_DELAY = 3;
    private static final int BASE_DAMAGE = 12;
    private static final int UPG_DAMAGE = 8;
    private static final int STUN_AMNT = 1;


    public Fissure() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = STUN_AMNT;
        this.tags.add(TheCaster.Enums.DELAYED_CARD);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
		actions.add(new VFXAction(new IronWaveEffect(p.hb.cX, p.hb.cY, Settings.WIDTH), 0.8f));
    	
		int[] damageMatrix = DamageInfo.createDamageMatrix(damage);
    	actions.add(new DamageAllEnemiesAction(p, damageMatrix, DamageType.NORMAL, AttackEffect.SMASH));
    	
    	for(AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
    		actions.add(new StunMonsterAction(mon, p));
    	}
    	
		AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, actions));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeDamage(UPG_DAMAGE);
        }
    }
}
