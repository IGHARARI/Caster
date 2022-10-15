package sts.caster.cards.skills;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;

import static sts.caster.core.CasterMod.makeCardPath;

public class CasterDefend extends CustomCard {

    public static final String ID = CasterMod.makeID("defend");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("defend.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int EXTRA_BLOCK = 2;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public CasterDefend() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = EXTRA_BLOCK;
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//    	int blockGain = block;
//    	if(AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().anyMatch(c -> c.type == CasterCardType.SPELL)) {
//    		blockGain += magicNumber;
//    	}
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
