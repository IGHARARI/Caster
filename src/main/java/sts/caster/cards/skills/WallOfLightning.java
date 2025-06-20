package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.core.freeze.ElectrifiedHelper;
import sts.caster.interfaces.ICardWasElectrifiedSubscriber;
import sts.caster.patches.delayedCards.CastingQueueGroupEnum;

import static sts.caster.core.CasterMod.makeCardPath;

public class WallOfLightning extends CasterCard implements ICardWasElectrifiedSubscriber {

    public static final String ID = CasterMod.makeID("WallOfLightning");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("walloflightning.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 6;
    private static final int BASE_BLOCK = 12;
    private static final int UPG_BLOCK = 4;

    public WallOfLightning() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = block = BASE_BLOCK;
        if (ElectrifiedHelper.getCardsElectrifiedThisCombat() > 0) {
            updateCost(-ElectrifiedHelper.getCardsElectrifiedThisCombat());
        }
        setCardElement(MagicElement.ELECTRIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        addToBot(new GainBlockAction(p, block));
    }

    public void cardWasElectrified(CardGroup.CardGroupType groupOfCard){
        if (groupOfCard != CastingQueueGroupEnum.CASTER_CASTING_QUEUE) {
            updateCost(-1);
        }
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
