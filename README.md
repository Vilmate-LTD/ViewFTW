# View For The Win!
The android base View implementation to get rid of Fragments. Inspired by the iconic Square article - https://medium.com/square-corner-blog/advocating-against-android-fragments-81fd0b462c97

This ViewFtw class has regular Activity/Fragment callbacks like onViewCreated, onStart/onStop. No onResume/onPause, but in real life we haven't had any issues with that.
If you absolutely need them, or want other lifecycle callbacks, you can throw them from the Activity.

And one helpful nicety - showAsDialog() method. Proven to be helpful during the development.