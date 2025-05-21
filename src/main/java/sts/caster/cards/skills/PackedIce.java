package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.IPlayableWhileFrozen;

import static sts.caster.core.CasterMod.makeCardPath;

public class PackedIce extends CasterCard implements IPlayableWhileFrozen {

    public static final String ID = CasterMod.makeID("PackedIce");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("packedice.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_BLOCK = 8;
    private static final int UPG_BLOCK = 3;
    private int unfrozenCost;

    public PackedIce() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = block = BASE_BLOCK;
        unfrozenCost = COST;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        addToBot(new GainBlockAction(p, block));
//        addToBot(new AbstractGameAction() {
//            @Override
//            public void update() {
//                if (AbstractDungeon.player.currentBlock >= m2) {
//                    ArrayList<AbstractMonster> mons = BattleHelper.getAliveMonsters();
//                    for (AbstractMonster m : mons) {
//                        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false)));
//                    }
//                }
//                isDone = true;
//            }
//        });
    }

    @Override
    public void onFrozen() {
        this.unfrozenCost = this.cost; // save original cost
        modifyCostForCombat(-999);
    }

    @Override
    public void onThaw() {
        modifyCostForCombat(this.unfrozenCost);
        this.isCostModified = false;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK);
            initializeDescription();
        }
    }
}
