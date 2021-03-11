package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static Spansion.Spansion.makeEventPath;

public class ComplexCardEvent extends AbstractImageEvent {

    public static final String ID = Spansion.makeID("ComplexCardEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Diary.png");

    private enum CUR_SCREEN{ INTRO, COMPLETE}

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    private boolean chooseUpgrade = false;
    private int upgrades = 2;
    private boolean chooseTransform = false;
    private boolean chooseDuplicate = false;

    public ComplexCardEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2] + upgrades + OPTIONS[3]);
    }

    @Override
    protected void buttonEffect(int i) {
        switch(screen){
            case INTRO:
                switch (i){
                    case 0: // Transform a card
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        AbstractDungeon.gridSelectScreen.open(
                                CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
                                .getPurgeableCards()), 1, OPTIONS[2], false, true, false, false);
                        chooseTransform = true;
                        break;
                    case 1: // Duplicate
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        chooseDuplicate = true;
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, OPTIONS[2], false, false, false, false);
                        break;
                    case 2: // Upgrade N Cards
                        //CardGroup group = AbstractDungeon.player.masterDeck
                        ArrayList<AbstractCard> upgradableCards = new ArrayList<AbstractCard>();
                        upgradableCards = AbstractDungeon.player.masterDeck.getUpgradableCards().group;
                        /*
                        for (AbstractCard c:AbstractDungeon.player.masterDeck.group ) {
                            if(c.canUpgrade())
                            {upgradableCards.add(c);}
                        }
                        */
                        Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
                        for(int j = 0; j < upgrades && j < upgradableCards.size(); j++) {
                            AbstractCard card = upgradableCards.get(j);
                            card.upgrade();
                            AbstractDungeon.player.bottledCardUpgradeCheck(card);
                            float offsetX = AbstractDungeon.miscRng.random(-200, 200);
                            float offsetY = AbstractDungeon.miscRng.random(-200, 200);
                            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()
                                    , Settings.WIDTH / 2.0F +offsetX * Settings.scale, Settings.HEIGHT / 2.0F + offsetY));
                        }
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        break;
                }
                imageEventText.updateDialogOption(0, OPTIONS[4]);
                imageEventText.clearRemainingOptions();
                screen = CUR_SCREEN.COMPLETE;
                break;
            case COMPLETE:
                AbstractDungeon.player.hand.clear();
                openMap();
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp &&
                !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            if (chooseTransform) {
                AbstractDungeon.player.masterDeck.removeCard(c);
                AbstractDungeon.transformCard(c, false, AbstractDungeon.miscRng);
                AbstractCard transCard = AbstractDungeon.getTransformedCard();
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(transCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            }
            if(chooseDuplicate) {
                AbstractCard dupe = c.makeStatEquivalentCopy();
                dupe.inBottleFlame = false;
                dupe.inBottleLightning = false;
                dupe.inBottleTornado = false;
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(dupe, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            }


            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }
}
