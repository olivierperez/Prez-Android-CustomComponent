# Prez-Android-CustomView

Presentation media for a talk about creation of Android Custom View.

# Why

- It easy do it wrong
- There are a lot of things to know

# Live-coding genda

- Create a simple view doing nothing
- IDE integration
- Handle measurement
- Draw rectangle
- Replace rectangle by the wheel
- Make the wheel colorful
- Implement a progression with animation
- Handle pin selection
- Make the screen scrollable when not editing
- TODO : AttributeSet

# What I've seen, but I should not

- Listener called in onDraw
- Domain-specific code inside the custom view
- a lot of "invalidate"

# Summary

- Extends View
- AttributeSet
- onMeasure / onDraw
- invalidate / postInvalidate / requestLayout
- Don't create new objects in onDraw method (60fps <=> 16.67ms per frame)
- Canvas are fun (rotate, translate, draw, save, restore, layers)
- onTouchEvent can prevent event propagation if you need to