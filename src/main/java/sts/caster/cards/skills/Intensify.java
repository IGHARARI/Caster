package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ModifyCastingSpellsEffectAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;

import static sts.caster.core.CasterMod.makeCardPath;

public class Intensify extends CasterCard {

    public static final String ID = CasterMod.makeID("Intensify");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("channeling.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_BOOST = 4;
    private static final int UPG_BOOST = 2;
    private static final int DRAW_AMOUNT = 1;

    public Intensify() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_BOOST;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ModifyCastingSpellsEffectAction(magicNumber, magicNumber));
        addToBot(new DrawCardAction(p, DRAW_AMOUNT));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeMagicNumber(UPG_BOOST);
        }
    }
}
