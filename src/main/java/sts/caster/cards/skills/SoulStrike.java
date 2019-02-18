package sts.caster.cards.skills;

import static sts.caster.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlyingOrbEffect;

import sts.caster.CasterMod;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.characters.TheCaster;

public class SoulStrike extends CasterCard {

    public static final String ID = CasterMod.makeID("SoulStrike");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int BASE_DELAY = 1;
    private static final int BASE_DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 5;


    public SoulStrike() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.isEthereal = true;
        baseDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns =  BASE_DELAY;
        tags.add(TheCaster.Enums.DELAYED_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
    	actions.add(new VFXAction(new FlyingOrbEffect(m.drawX, m.drawY)));
    	actions.add(new DamageAction(m, new DamageInfo(p, this.damage)));
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
