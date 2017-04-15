---
layout: docs
title:  "Type Classes"
section: "typeclasses"
position: 3
---
# Type Classes 

{% for x in site.pages %}
  {% if x.section == 'algorithms' and x.title != page.title %}
- [{{x.title}}]({{site.baseurl}}{{x.url}})
  {% endif %}
{% endfor %}
