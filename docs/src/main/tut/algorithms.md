---
layout: docs
title:  "Algorithms"
section: "algorithms"
position: 1
---
# Data Structures 

{% for x in site.pages %}
  {% if x.section == 'algorithms' and x.title != page.title %}
- [{{x.title}}]({{site.baseurl}}{{x.url}})
  {% endif %}
{% endfor %}
