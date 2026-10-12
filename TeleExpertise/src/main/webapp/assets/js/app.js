document.querySelectorAll('input, textarea, select').forEach(function (field) {
    if (!field.id) field.id = field.name;
    const label = field.previousElementSibling;
    if (label && label.tagName === 'LABEL') label.htmlFor = field.id;
});
