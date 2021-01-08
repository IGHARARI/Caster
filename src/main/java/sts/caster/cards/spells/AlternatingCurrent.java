package sts.caster.cards.spells;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import sts.caster.actions.DelayedDamageRandomEnemyAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;
import sts.caster.interfaces.MonsterToActionInterface;
import sts.caster.patches.spellCardType.CasterCardType;

public class AlternatingCurrent extends CasterCard {

    public static final String ID = CasterMod.makeID("AlternatingCurrent");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("alternatingcurrent.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CasterCardType.SPELL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 1;
    private static final int BASE_DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;


    public AlternatingCurrent() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseSpellDamage = spellDamage = BASE_DAMAGE;
        delayTurns = baseDelayTurns =  BASE_DELAY;
        setCardElement(MagicElement.THUNDER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new QueueDelayedCardAction(this, delayTurns, null));
    }
    
    @Override
    public ActionListMaker buildActionsSupplier(Integer energySpent) {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
            ArrayList<MonsterToActionInterface> lightningEffects = new ArrayList<MonsterToActionInterface>();
            lightningEffects.add(m -> new VFXAction(new LightningEffect(m.drawX, m.drawY), 0f));
            lightningEffects.add(m -> new SFXAction("ORB_LIGHTNING_EVOKE"));
    		actions.add(new DelayedDamageRandomEnemyAction(AbstractDungeon.player, c.spellDamage, c.cardElement, AttackEffect.NONE, lightningEffects));
        	actions.add(new QueueDelayedCardAction(c, BASE_DELAY, t));
    		return actions;
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
