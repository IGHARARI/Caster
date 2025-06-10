package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.interfaces.ICardWasElectrifiedSubscriber;
import sts.caster.powers.LightningRodPower;

import static sts.caster.core.CasterMod.makeCardPath;

public class LightningRod extends CasterCard implements ICardWasElectrifiedSubscriber {

    public static final String ID = CasterMod.makeID("LightningRod");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("ashes.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int UPGR_COST = 0;
    private static final int BASE_APPLY = 4;

    public LightningRod() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_APPLY;
        setCardElement(MagicElement.ELECTRIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        addToBot(new ApplyPowerAction(monster, p, new LightningRodPower(monster, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGR_COST);
            initializeDescription();
        }
    }

    @Override
    public void cardWasElectrified(CardGroup.CardGroupType thisCardGroup) {
        if (thisCardGroup == CardGroup.CardGroupType.DISCARD_PILE) {
            LightningRod thisCard = this;
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    if (AbstractDungeon.player.discardPile.contains(thisCard)) {
                        AbstractDungeon.player.discardPile.moveToDeck(thisCard, false);
                    }
                    isDone = true;
                }
            });
        }
    }
}
