package sts.caster.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.core.freeze.IgnitedHelper;
import sts.caster.powers.IzanagiPower;

import static sts.caster.core.CasterMod.makeCardPath;

public class IzanagisObdurance extends CasterCard {

    public static final String ID = CasterMod.makeID("IzanagisObdurance");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("izanagi.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int TURN_IGNITE = 1;
    private static final int ONPLAY_IGNITE = 2;

    public IzanagisObdurance() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = TURN_IGNITE;
        m2 = baseM2 = ONPLAY_IGNITE;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IzanagiPower(p, magicNumber), magicNumber));
        if (upgraded) {
            addToBot(IgnitedHelper.buildSelectCardsToIgniteAction(ONPLAY_IGNITE));
        }
    }

    @Override
    public float getTitleFontSize() {
        return 20;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}