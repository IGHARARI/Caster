package sts.caster.cards.skills;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.IgniteSpecificCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.core.freeze.IgnitedHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static sts.caster.core.CasterMod.makeCardPath;

public class MatchBox extends CasterCard {

    public static final String ID = CasterMod.makeID("MatchBox");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("matchbox.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 0;
    private static final int IGNITE_AMOUNT = 1;
    private static final int UPGRADE_IGNITE_AMOUNT = 1;
    private static final int BASE_DAMAGE = 14;

    public MatchBox() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        isInnate = true;
        exhaust = true;
        baseMagicNumber = magicNumber = IGNITE_AMOUNT;
        baseDamage = damage = BASE_DAMAGE;
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Predicate<AbstractCard> isDamageCard = c -> {
            return (
                    c.type == CardType.ATTACK ||
                    (c instanceof CasterCard && ((CasterCard) c).spellDamage > 0)
            );
        };
        Consumer<List<AbstractCard>> addIgniteToCards = list -> {
            list.forEach(c -> {
                c.flash(Color.RED.cpy());
                addToTop(new IgniteSpecificCardAction(c));
            });
        };
        for (int i = 0; i < magicNumber; i++) {
            addToBot(IgnitedHelper.buildSelectCardsToIgniteAction(1, isDamageCard));
        }
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_IGNITE_AMOUNT);
            initializeDescription();
        }
    }
}
