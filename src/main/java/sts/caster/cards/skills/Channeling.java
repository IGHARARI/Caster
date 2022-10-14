package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.patches.spellCardType.CasterCardType;

import static sts.caster.core.CasterMod.makeCardPath;

public class Channeling extends CasterCard {

    public static final String ID = CasterMod.makeID("Channeling");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("channeling.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int BASE_BLOCK = 2;
    private static final int UPG_BLOCK = 1;
    private static final int EXTRA_BLOCK_DIFF = 1;
    private static final int UPG_EXTRA_BLOCK = 1;


    public Channeling() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BASE_BLOCK;
        magicNumber = baseMagicNumber = BASE_BLOCK + EXTRA_BLOCK_DIFF;
    }

    @Override
    public void applyPowers() {
        int blockDiff = EXTRA_BLOCK_DIFF + this.timesUpgraded * UPG_EXTRA_BLOCK;
        this.baseBlock += blockDiff;
        this.baseMagicNumber = this.baseBlock;
        super.applyPowers();
        this.magicNumber = this.block;
        this.isMagicNumberModified = this.isBlockModified;
        this.baseBlock -= blockDiff;
        super.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().anyMatch(c -> c.type == CasterCardType.SPELL)) {
            addToBot(new GainBlockAction(p, p, magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeBlock(UPG_BLOCK);
            this.baseMagicNumber = BASE_BLOCK + EXTRA_BLOCK_DIFF + this.timesUpgraded * UPG_EXTRA_BLOCK;
            this.upgradedMagicNumber = this.upgradedBlock;
        }
    }
}
