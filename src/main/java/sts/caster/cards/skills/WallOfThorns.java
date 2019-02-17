package sts.caster.cards.skills;

import static sts.caster.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import sts.caster.CasterMod;
import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.characters.TheCaster;

public class WallOfThorns extends CasterCard {

    public static final String ID = CasterMod.makeID("WallOfThorns");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 2;
    private static final int BASE_BLOCK = 7;
    private static final int UPG_BLOCK = 2;
    private static final int BASE_THORNS = 3;


    public WallOfThorns() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_THORNS;
        baseBlock = BASE_BLOCK;
        baseDelayTurns = delayTurns = BASE_DELAY;
        this.tags.add(TheCaster.Enums.DELAYED_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    	ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
    	actions.add(new GainBlockAction(p, p, block));
    	actions.add(new GainBlockAction(p, p, block));
    	actions.add(new ApplyPowerAction(p, p, new ThornsPower(p, magicNumber), magicNumber));
		AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, actions));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeBlock(UPG_BLOCK);
        }
    }
}
