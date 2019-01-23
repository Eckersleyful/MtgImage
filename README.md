So this was a bit silly thing I wrote during a few lectures. First I wanted to pull a random MTG card from their card library database and
display it.
Sure that works. Only problem was that in that same image database were thousands of pictures of just cardbacks.
I didn't want my program to display them so I thought, hey maybe there is a suffix in the picture URL to differentiate
the cardbacks from the actual card images. Boy, I was wrong.
So I went through a hoop of comparing the two card pixel by pixel. I used a standard cardback image for comparison and
every time the program pulls an image from the database, it compares it with the picture of the cardback. If they differ over 10 percent
we can safely say that the pulled card, infact is a card and not a cardback.
