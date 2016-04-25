package tv.teads.github.api.services.repositories

import tv.teads.github.api.services.AbstractServiceSpec

class RepositoriesStatisticsServiceSpec extends AbstractServiceSpec {

  "contributorsStatistics" should {
    "be able to fetch statistics for github-api-client_test" in {
      eventually {
        whenReady(teadsClient.repositories.statistics.contributorsStatistics("github-api-client_test")) { stats ⇒
          stats.find(_.author.login == "pdalpra") should not be empty
        }
      }
    }

    "not be able to fetch statistics for a non existing repository" in {
      eventually {
        whenReady(teadsClient.repositories.statistics.contributorsStatistics("foobarquz")) { stats ⇒
          stats shouldBe empty
        }
      }
    }
  }

  "lastYearCommitActivity" should {
    "be able to fetch last year's commit activity for github-api-client_test" in {
      eventually {
        whenReady(teadsClient.repositories.statistics.lastYearCommitActivity("github-api-client_test")) { stats ⇒
          // Check that data is well formed
          stats.foreach { weekStats ⇒
            weekStats.days.sum shouldBe weekStats.total
          }
        }
      }
    }

    "not be able to fetch last year's commit activity for a non existing repository" in {
      eventually {
        whenReady(teadsClient.repositories.statistics.lastYearCommitActivity("foobarquz")) { stats ⇒
          stats shouldBe empty
        }
      }
    }
  }

  "weeklyAdditionsDeletions" should {
    "be able to fetch last weekly additions/deletions for github-api-client_test" in {
      eventually {
        whenReady(teadsClient.repositories.statistics.weeklyAdditionsDeletions("github-api-client_test")) { stats ⇒
          // Check that data is well formed
          stats.foreach { weekStats ⇒
            weekStats.additions should be >= 0
            weekStats.deletions should be >= 0
          }
        }
      }
    }

    "not be able to fetch weekly additions/deletions for a non existing repository" in {
      eventually {
        whenReady(teadsClient.repositories.statistics.weeklyAdditionsDeletions("foobarquz")) { stats ⇒
          stats shouldBe empty
        }
      }
    }
  }

  "lastYearWeeklyCommitCount" should {
    "be able to fetch last year's weekly commit count for github-api-client_test" in {
      eventually {
        whenReady(teadsClient.repositories.statistics.lastYearWeeklyCommitCount("github-api-client_test")) { stats ⇒
          // Check that data is well formed
          stats.value.all.size shouldBe stats.value.owner.size
        }
      }
    }

    "not be able to fetch last year's weekly commit count for a non existing repository" in {
      eventually {
        whenReady(teadsClient.repositories.statistics.lastYearWeeklyCommitCount("foobarquz")) { stats ⇒
          stats shouldBe empty
        }
      }
    }
  }

  "hourlyCommitCount" should {
    "be able to fetch hourly commit count for github-api-client_test" in {
      eventually {
        whenReady(teadsClient.repositories.statistics.hourlyCommitCount("github-api-client_test")) { stats ⇒
          // Check that data is well formed
          val statsByDay = stats.groupBy(_.dayOfWeek)
          statsByDay.keys should have size 7
          statsByDay.values foreach { dailyStats ⇒
            dailyStats should have size 24
          }
        }
      }
    }

    "not be able to fetch hourly commit count for a non existing repository" in {
      eventually {
        whenReady(teadsClient.repositories.statistics.hourlyCommitCount("foobarquz")) { stats ⇒
          stats shouldBe empty
        }
      }
    }
  }
}
