package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.actions.ModifyCardDamageAction;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.CasterCardTags;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ActionListMaker;

public class Explosion extends CasterCard {

    public static final String ID = CasterMod.makeID("Explosion");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("explosion.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 3;
    private static final int BASE_DAMAGE = 30;
    private static final int BASE_DOWNGRADE = 10;
    private static final int UPG_DOWNGRADE = -5;


    public Explosion() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDelayTurns = delayTurns = BASE_DELAY;
        baseSpellDamage = spellDamage =  BASE_DAMAGE;
        magicNumber = baseMagicNumber = BASE_DOWNGRADE;
        cardElement = MagicElement.FIRE;
        this.tags.add(CasterCardTags.DELAYED_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, m));
    }
    
    @Override
    public ActionListMaker getActionsMaker() {
    	return (c, t) -> {
    		ArrayList<AbstractGameAction> actionsList = new ArrayList<AbstractGameAction>();
    		actionsList.add(new DamageAction(t, new DamageInfo(AbstractDungeon.player, c.spellDamage), AttackEffect.FIRE));
    		actionsList.add(new ModifyCardDamageAction(this, c.magicNumber));
    		return actionsList;
    	};
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeMagicNumber(UPG_DOWNGRADE);
        }
    }
}
