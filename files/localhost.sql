INSERT INTO gags_v2_videos (href,src, alt,tags,lang) SELECT video_src,video_src, title ,'[{"tags":"9gags"}]', "en" FROM funny_videos_data;

--
-- Table structure for table `gags_v2_anim`
--

DROP TABLE IF EXISTS `gags_v2_anim`;
CREATE TABLE IF NOT EXISTS `gags_v2_anim` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_request_id` int(11) NOT NULL,
  `href` text NOT NULL,
  `src` text NOT NULL,
  `alt` varchar(250) NOT NULL,
  `tags` varchar(100) NOT NULL,
  `lang` varchar(2) NOT NULL,
  `active` int(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `gags_v2_img`
--

DROP TABLE IF EXISTS `gags_v2_img`;
CREATE TABLE IF NOT EXISTS `gags_v2_img` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_request_id` int(11) NOT NULL,
  `href` text NOT NULL,
  `src` text NOT NULL,
  `alt` varchar(250) NOT NULL,
  `tags` varchar(100) NOT NULL,
  `lang` varchar(2) NOT NULL,
  `active` int(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `gags_v2_jokes`
--

DROP TABLE IF EXISTS `gags_v2_jokes`;
CREATE TABLE IF NOT EXISTS `gags_v2_jokes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_request_id` int(11) NOT NULL,
  `title` varchar(250) NOT NULL,
  `text` longtext NOT NULL,
  `tags` varchar(100) NOT NULL,
  `lang` varchar(2) NOT NULL,
  `active` int(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `gags_v2_videos`
--

DROP TABLE IF EXISTS `gags_v2_videos`;
CREATE TABLE IF NOT EXISTS `gags_v2_videos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_request_id` int(11) NOT NULL,
  `href` text NOT NULL,
  `src` text NOT NULL,
  `alt` varchar(250) NOT NULL,
  `tags` varchar(100) NOT NULL,
  `lang` varchar(2) NOT NULL,
  `active` int(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `gags_video_json`
--

DROP TABLE IF EXISTS `gags_video_json`;
CREATE TABLE IF NOT EXISTS `gags_video_json` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_request_id` varchar(50) NOT NULL,
  `jsons` longtext NOT NULL,
  `active` int(1) DEFAULT '1',
  `lang` varchar(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=992 ;

--
-- Dumping data for table `gags_video_json`
--