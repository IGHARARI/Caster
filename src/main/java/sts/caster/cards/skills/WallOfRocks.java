package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.powers.WeakPower;

import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

public class WallOfRocks extends CasterCard {

    public static final String ID = CasterMod.makeID("WallOfRocks");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("wallofrock.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BLOCK_AMT = 7;
    private static final int UPG_BLOCK_AMT = 3;
    private static final int WEAK_AMT = 3;
    
    public WallOfRocks() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = WEAK_AMT;
        setCardElement(MagicElement.EARTH);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new GainBlockAction(p, p, block));
    	for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
    		if (mon.isDeadOrEscaped()) continue;
    		if ((mon.intent == Intent.ATTACK || mon.intent == Intent.ATTACK_BUFF || mon.intent == Intent.ATTACK_DEBUFF || mon.intent == Intent.ATTACK_DEFEND)) {
    			addToBot(new ApplyPowerAction(mon, p, new WeakPower(mon, magicNumber, false), magicNumber));
    		}
    	}
    }
    
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK_AMT);
            initializeDescription();
        }
    }
}
