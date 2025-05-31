package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.powers.WallOfRocksPower;

import static sts.caster.core.CasterMod.makeCardPath;

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

    private static final int COST = 2;
    private static final int BLOCK_AMT = 11;
    private static final int UPG_BLOCK_AMT = 3;
    private static final int MAX_TRIGGERS = 3;
    private static final int STR_LOSS_AMT = 2;
    private static final int STR_LOSS_UGPR = 1;

    public WallOfRocks() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK_AMT;
        magicNumber = baseMagicNumber = STR_LOSS_AMT;
        this.exhaust = true;
        setCardElement(MagicElement.NEUTRAL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new WallOfRocksPower(p, MAX_TRIGGERS, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK_AMT);
            upgradeMagicNumber(STR_LOSS_UGPR);
            initializeDescription();
        }
    }
}
