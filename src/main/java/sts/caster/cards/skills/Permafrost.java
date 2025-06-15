package sts.caster.cards.skills;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ModifyAllCastingSpellCastTimeAction;
import sts.caster.cards.CasterCard;
import sts.caster.cards.mods.FreezeOnUseCardMod;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.powers.CannotLoseHpPower;

import static sts.caster.core.CasterMod.makeCardPath;

public class Permafrost extends CasterCard {

    public static final String ID = CasterMod.makeID("Permafrost");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("permafrost.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;


    private static final int CAST_TIME_INCREASE = 1;
    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;

    public Permafrost() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        addToBot(new ModifyAllCastingSpellCastTimeAction(CAST_TIME_INCREASE));
        addToBot(new ApplyPowerAction(p, p, new CannotLoseHpPower(p, 1)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            upgradeBaseCost(UPGRADED_COST);
            CardModifierManager.addModifier(this, new FreezeOnUseCardMod(false));
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
