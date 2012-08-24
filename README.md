# URL Filter

By default, Sling applications allow any combination of selectors, extensions, and suffixes to be served. Our general recommendation is to solve this problem at the dispatcher, but this can be difficult to manage sometimes, largely because the allowable selectors (and extensions and suffixes) are really a property of the content type (in Sling verbiage, the resource type; in CQ verbiage, the component type), not necessarily the path (which is all the dispatcher knows about).

This filter implements a generic strategy for handling selector, extension, and suffix filtering within Sling. It is based on a Sling sample project [here](http://svn.apache.org/repos/asf/sling/trunk/samples/urlfilter/). That sample project does not work with CQ 5.5 because it relies on being able to read properties from resource type definition nodes (in CQ, these are cq:Component nodes). In CQ 5.5, ACLs have been put into place which prevents end-user access to these nodes.

This version of the filter uses the CQ `ComponentManager` to read these properties.

## Configuration

Once installed, this filter needs to be configured with a set of properties. Generally, these would be set on the page component node. The properties are:

* `allowedExtensions` (String[]) - one or more allowed extensions
* `allowedExtensionPattern` (String) - a RegEx defining a pattern used to allow extensions
* `allowedSuffixes` (String[]) - one or more allowed suffixes
* `allowedSuffixPattern` (String) - a RegEx defining a pattern used to allow suffixes
* `allowedSelectors` (String[]) - one or more allowed selectors
* `allowedSelectorPattern` (String) - a RegEx defining a pattern used to allow selectors

The component hierarchy (i.e. sling:resourceSuperType) is respected, but there is no support for combining these properties - the first component which defines any of these properties "wins" and is used as the configuration.

A single blank property value will prevent all values for the corresponding path component from being allowed, e.g. to prevent all selectors, set allowedSelectors to "".