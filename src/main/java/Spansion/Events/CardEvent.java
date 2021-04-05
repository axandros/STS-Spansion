package Spansion.Events;

import Spansion.Spansion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static Spansion.Spansion.makeEventPath;

public class CardEvent extends AbstractImageEvent {

    public static final String ID = Spansion.makeID("CardEvent");
    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = makeEventPath("Diary.png");

    private enum CUR_SCREEN{ INTRO, COMPLETE}

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;
    private boolean ChooseCardAdd = false;
    private boolean ChooseCardRemove = false;
    private AbstractCard offeredCard;

    public CardEvent() {
        super(NAME, DESCRIPTIONS[0], IMG);

        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2]);
        imageEventText.setDialogOption(OPTIONS[3]);
    }

    @Override
    public void update() {
        super.update();
        if (ChooseCardAdd && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = ((AbstractCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0)).makeCopy();
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        if (ChooseCardRemove && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                offeredCard = AbstractDungeon.gridSelectScreen.selectedCards.remove(0);
                AbstractDungeon.player.masterDeck.removeCard(this.offeredCard);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
            }
    }

    @Override
    protected void buttonEffect(int i) {
        switch(screen){
            case INTRO:
                switch (i){
                    case 0: // Add a card to your deck
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        CardGroup group = MakeRandomCardList(20);
                        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[2], false, false, false, false);
                        ChooseCardAdd = true;
                        break;
                    case 1: // Remove a card
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0)
                        {
                            AbstractDungeon.gridSelectScreen.open(
                                    CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards())
                                    , 1, OPTIONS[3], false, false, false, true);
                        }
                        ChooseCardRemove = true;
                        break;
                    case 2: // Add a skill
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        ChooseCardAdd = true;
                        CardGroup skillGroup = MakeRandomCardListSkills(3);
                        AbstractDungeon.gridSelectScreen.open(skillGroup, 1, OPTIONS[2], false, false, false, false);
                        break;
                    case 3: // Remove a skill
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        ChooseCardRemove = true;
                        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0)
                        {
                            AbstractDungeon.gridSelectScreen.open(
                                    CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).getSkills()
                                    , 1, OPTIONS[3], false, false, false, true);
                        }
                        break;
                }
                imageEventText.updateDialogOption(0, OPTIONS[4]);
                imageEventText.clearRemainingOptions();
                screen = CUR_SCREEN.COMPLETE;
                break;
            case COMPLETE:
                MonsterGroup mg = AbstractDungeon.getMonsters();
                AbstractDungeon.player.hand.clear();
                openMap();
                break;
        }
    }

    protected CardGroup MakeRandomCardList(int size){
        // unspecified as opposed to discard, deck, card pool, etc.
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (int i = 0; i < size; i++) {
            AbstractCard card = AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();

            boolean containsDupe = true;
            while (containsDupe) {
                containsDupe = false;


                for (AbstractCard c : group.group) {
                    if (c.cardID.equals(card.cardID)) {
                        containsDupe = true;
                        Spansion.logger.info("Dup is True " + card.cardID);
                        card = AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();
                    }
                }
            }

//            if (!group.contains(card)) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onPreviewObtainCard(card);
                }
                group.addToBottom(card);
//            } else {
//                Spansion.logger.info("Found 'duplicate' " + card.cardID);
 //               i--;
 //           }
        }
        return group;
    }
    protected CardGroup MakeRandomCardListSkills(int size){
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (int i = 0; i < size; i++) {
            AbstractCard card;// = AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();

            boolean duplicate = false;
            do {
                card = AbstractDungeon.getCard(AbstractDungeon.rollRarity()).makeCopy();
                duplicate = false;
                if (card.type == AbstractCard.CardType.SKILL) {
                    for (AbstractCard c : group.group) {
                        if (c.cardID.equals(card.cardID)) {
                            duplicate = true;
                        }
                    }
                }
            } while (!duplicate && card.type != AbstractCard.CardType.SKILL);

                if (!group.contains(card)) {
                    for (AbstractRelic r : AbstractDungeon.player.relics) {
                        r.onPreviewObtainCard(card);
                    }
                    group.addToBottom(card);
                } else {
                    i--;
                }
            }
        return group;
    }
}
